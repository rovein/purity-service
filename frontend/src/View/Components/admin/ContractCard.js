import React from 'react'
import { withTranslation } from 'react-i18next'
import jwt_decode from "jwt-decode"
import Moment from 'moment';
import localization from 'moment/locale/uk'
import Loader from "react-loader-spinner";

if(localStorage.getItem("Token") != null){
    var token = localStorage.getItem("Token")
    var decoded = jwt_decode(token)
}

var url = "http://localhost:8080"

class Card extends React.Component{
    constructor(props) {
        super(props);
        this.state = {
          error: null,
          isLoaded: false,
          contracts: [],
          services: {},
          date: ''
        };
      }
    
      getData(resUrl) {
        fetch(resUrl, {
                        method: 'get',
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json',
                            'Authorization': 'Bearer ' + token
                        }
                    })
          .then(res => res.json())
          .then(
            (result) => {
              this.setState({
                isLoaded: true,
                contracts: result
              });
            },
            (error) => {
              this.setState({
                isLoaded: true,
                error
              });
            }
          )
      }
      

      componentDidMount() {
        if(decoded.role === "PLACEMENT_OWNER"){
            this.getData(`${url}/contracts/placement-owner/${decoded.email}`);
        }else if(decoded.role === "CLEANING_PROVIDER"){
            this.getData(`${url}/contracts/cleaning-provider/${decoded.email}`);
        }
      }
    
      render() {
        const {t} = this.props
        const { error, isLoaded, contracts } = this.state;
        if (error) {
          return <div className='additional'>{t("Failiture")}: {error.message}</div>;
        } else if (!isLoaded) {
          return <div className="centered">
            <Loader
              type="Oval" //Audio Oval ThreeDots
              color="#4B0082"
              height={450}
              width={450}
              timeout={10000}
            />
          </div>;
        } else {
          return (
            <table className="w3-table-all w3-centered">
              <thead>
              <tr>
                <th>ID</th>
                <th>{t("sId")}</th>
                <th>{t("serviceName")}</th>
                {decoded.role === "PLACEMENT_OWNER" && <th>{t("CComp")}</th>}
                {decoded.role === "CLEANING_PROVIDER" && <th>{t("Comp")}</th>}
                <th>{t("rId")}</th>
                <th>{t("Price")}</th>
                <th>{t("CDate")}</th>
              </tr>
              </thead>
              <tbody>
              {contracts.sort((oneContract, anotherContract) => {
                return oneContract.id - anotherContract.id
              }).map(this.renderCard)}
              </tbody>
            </table>
          );
        }
      }

      localTime(date){
        if(localStorage.getItem("i18nextLng") === 'EN'){
          this.state.date = Moment(date).locale('en').format('LLL')
      }else if(localStorage.getItem("i18nextLng") === 'UA'){
         this.state.date = Moment(date).locale("uk", localization).format('LLL') 
      }
    }

    renderCard = (contract) => {
      const {t} = this.props
      const columnStyle = {verticalAlign: "middle"};
      this.localTime(contract.date)
        return (
          <tr>
            <td style={columnStyle}>{contract.id}</td>
            <td style={columnStyle}>{contract.providerServiceId}</td>
            <td style={columnStyle}>{contract.serviceName}</td>
            {
              decoded.role === "PLACEMENT_OWNER" &&
              <td style={columnStyle}>{contract.cleaningProviderName}</td>
            }
            {
              decoded.role === "CLEANING_PROVIDER" &&
              <td style={columnStyle}>{contract.placementOwnerName}</td>
            }
            <td style={columnStyle}>{contract.placementId}</td>
            <td style={columnStyle}>{contract.price}</td>
            <td style={columnStyle}>{this.state.date}</td>
          </tr>
        );
      };
}

export default withTranslation()  (Card);
