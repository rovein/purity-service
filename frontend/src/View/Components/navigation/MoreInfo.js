import React from 'react'
import Header from '../auth/HeaderAuth'
import { withTranslation } from 'react-i18next';

var url = "http://localhost:8080"

class Info extends React.Component{


    constructor(props) {
        super(props);
        this.state = {
          error: null,
          isLoaded: false,
          room: {},
          device:{}
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
                device:result.smartDevice
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
                return(
                    <div className="profile">
                        <Header/>
                <div className="profile_back">
                    <p id="cName">{this.state.room.placementType}</p>
                    <p></p>
                    <p>{t("AirQ")}: {this.state.device.airQuality}</p>
                    <p>{t("Temp")}: {this.state.device.temperature}</p>
                    <p>{t("Hum")}: {this.state.device.humidity}</p>
                    <p>{t("AdjFact")}: {this.state.device.adjustmentFactor}</p>
                    <p>{t("dFact")}: {this.state.device.dirtinessFactor}</p>
                </div>
                </div>)
        }
}

export default withTranslation() (Info);
