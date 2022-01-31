import React from 'react'
import { Line, Circle } from 'rc-progress';
import {withTranslation} from 'react-i18next';
import Loader from "react-loader-spinner";
import Button from "../../ui/Button";
import Moment from "moment";
import localization from 'moment/locale/uk';
import * as Constants from "../../util/Constants";

const url = Constants.SERVER_URL;

class Info extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      error: null,
      isLoaded: false,
      showIndicators: true,
      showContracts: false,
      room: {},
      device: {},
      contracts: []
    };
  }

  componentDidMount() {
    fetch(`${url}/placement-owners/placements/${localStorage.getItem("Id")}`, {
      method: 'get',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + localStorage.getItem('Token')
      }
    })
      .then(res => res.json())
      .then(
        (result) => {
          this.setState({
            isLoaded: true,
            room: result,
            device: result.smartDevice
          });
        },
        (error) => {
          this.setState({
            isLoaded: true,
            error
          });
        }
      );

    const email = JSON.parse(localStorage.getItem("placementOwner")).email

    fetch(`${url}/contracts/placement-owner/${email}`, {
      method: 'get',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + localStorage.getItem('Token')
      }
    })
      .then(res => res.json())
      .then(  (result) => {
          this.setState({
            contracts: result.filter(contract => {
              return contract.placementId === Number.parseInt(localStorage.getItem("Id"))
            }),
          });
          console.log(this.state.contracts)
        },
        (error) => {
          this.setState({
            error
          });
        })
  }

  render() {
    const {t} = this.props
    if (!this.state.isLoaded) {
      return <div className="centered" style={{marginLeft: '34%'}}>
        <Loader
          type="Oval" //Audio Oval ThreeDots
          color="#4B0082"
          height={500}
          width={500}
          timeout={10000}
        />
      </div>;
    }
    return (<div>
        <div className="profile_back">
          <p id="cName">
            {t("Rooms")} № {this.state.room.id}, {this.state.room.placementType}<br/>
          </p>
          <p></p>
          <p>
            {t("Temp")}: {this.state.device.temperature} ℃<br/><br/>
            {t("Hum")}: {this.state.device.humidity} %<br/><br/>
            {t("AdjFact")}: {this.state.device.adjustmentFactor}
          </p>
          <p>
            <Button
              text = "Переглянути показники"
              disabled = {false}
              onClick = { () => {this.setState({showIndicators: true, showContracts: false})}}
            />
            <Button
              text = "Переглянути угоди"
              disabled = {false}
              onClick = { () => {this.setState({showIndicators: false, showContracts: true})}}
            />
          </p>
        </div>
        { this.state.showIndicators &&
          <div className={"progress-grid"}>
            <div>
              <h2
                style={{paddingLeft: "60px"}}>{t("AirQ")}: {this.state.device.airQuality * 100} %</h2>
              <Circle style={{width: 400, height: 400}}
                      percent={this.state.device.airQuality * 100}
                      strokeWidth="4" strokeColor="#2d52f5"/>
            </div>
            <div>
              <h2>{t("dFact")}: {this.state.device.dirtinessFactor * 100} %</h2>
              <Circle style={{width: 400, height: 400}}
                      percent={this.state.device.dirtinessFactor * 100}
                      strokeWidth="4" strokeColor="#2d52f5"/>
            </div>
          </div>
        }
        {this.state.showContracts && <table className="w3-table-all w3-centered w3-large">
          <thead>
          <tr>
            <th>ID</th>
            <th>{t("sId")}</th>
            <th>{t("serviceName")}</th>
            <th>{t("CComp")}</th>
            <th>{t("rId")}</th>
            <th>{t("Price")}</th>
            <th>{t("CDate")}</th>
          </tr>
          </thead>
          <tbody>
          {this.state.contracts.sort((oneContract, anotherContract) => {
            return oneContract.id - anotherContract.id
          }).map((contract) => {
            const columnStyle = {verticalAlign: "middle"};
            this.localTime(contract.date)
            return (
              <tr>
                <td style={columnStyle}>{contract.id}</td>
                <td style={columnStyle}>{contract.providerServiceId}</td>
                <td style={columnStyle}>{contract.serviceName}</td>
                <td style={columnStyle}>{contract.cleaningProviderName}</td>
                <td style={columnStyle}>{contract.placementId}</td>
                <td style={columnStyle}>{contract.price} ₴</td>
                <td style={columnStyle}>{this.state.date}</td>
              </tr>
            );
          })}
          </tbody>
        </table>
        }
      </div>
    )
  }

  localTime(date) {
    if (localStorage.getItem("i18nextLng") === 'EN') {
      this.state.date = Moment(date).locale('en').format('LLL')
    } else if (localStorage.getItem("i18nextLng") === 'UA') {
      this.state.date = Moment(date).locale("uk", localization).format('LLL')
    }
  }
}

export default withTranslation()(Info);
