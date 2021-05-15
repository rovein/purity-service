import React from 'react'
import { Line, Circle } from 'rc-progress';
import {withTranslation} from 'react-i18next';
import Loader from "react-loader-spinner";

var url = "http://localhost:8080"

class Info extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      error: null,
      isLoaded: false,
      room: {},
      device: {}
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
      )
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
          <p id="cName">{t("Rooms")} № {this.state.room.id}, {this.state.room.placementType}</p>
          <p></p>
          <p>{t("Temp")}: {this.state.device.temperature} ℃</p>
          <p></p>
          <p>{t("Hum")}: {this.state.device.humidity} %</p>
          <p></p>
          <p>{t("AdjFact")}: {this.state.device.adjustmentFactor}</p>
        </div>
        <div className={"progress-grid"}>
          <div>
            <h2 style={{paddingLeft: "60px"}}>{t("AirQ")}: {this.state.device.airQuality * 100} %</h2>
            <Circle style={{width:400, height: 400}} percent={this.state.device.airQuality * 100} strokeWidth="4" strokeColor="#2d52f5" />
          </div>
         <div>
           <h2>{t("dFact")}: {this.state.device.dirtinessFactor * 100} %</h2>
          <Circle style={{width:400, height: 400}} percent={this.state.device.dirtinessFactor * 100} strokeWidth="4" strokeColor="#2d52f5" />
         </div>
        </div>
      </div>
    )
  }
}

export default withTranslation()(Info);
