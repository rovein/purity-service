import React from 'react'
import { withTranslation } from 'react-i18next'
import jwt_decode from "jwt-decode"
import Moment from 'moment';
import localization from 'moment/locale/uk'

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
          return <div className='additional'>{t("Loading")}...</div>;
        } else {
          return (
            <div className="grid">
              {contracts.map(this.renderCard)}
              </div>
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

    getName(){

    }


    renderCard = (contract) => {
      const {t} = this.props
      this.localTime(contract.date)
        return (
          <div className="card text-center">
                      <div className="crd-body text-dark" id ={contract.id}>
                          <h2 className="card-title">{t('Contract')}</h2>
                          <p className="card-text text-secondary">{t("sId")}: {contract.providerServiceId}</p>
                          <p className="card-text text-secondary">{t("serviceName")}: {contract.serviceName}</p>
                          {
                              decoded.role === "PLACEMENT_OWNER" &&
                              <p className="card-text text-secondary">{t("CComp")}: {contract.cleaningProviderName}</p>
                          }
                          {
                              decoded.role === "CLEANING_PROVIDER" &&
                              <p className="card-text text-secondary">{t("Comp")}: {contract.placementOwnerName}</p>
                          }
                          <p className="card-text text-secondary">{t("rId")}: {contract.placementId}</p>
                          <p className="card-text text-secondary">{t("Price")}: {contract.price} {t("pMes")}</p>
                          <p className="card-text text-secondary">{t("CDate")}: {this.state.date}</p>

                      </div>
                  </div>
        );
      };
}

export default withTranslation()  (Card);
