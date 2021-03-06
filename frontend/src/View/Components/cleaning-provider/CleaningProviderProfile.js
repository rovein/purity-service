import React from 'react'
import { withTranslation } from 'react-i18next';

import jwt_decode from "jwt-decode"
import ServiceCard from './service/ServicesTable';
import * as Constants from "../util/Constants";

if(localStorage.getItem("Token") != null){
    var token = localStorage.getItem("Token")
    var decoded = jwt_decode(token)
}

const url = Constants.SERVER_URL;

class Profile extends React.Component{

    constructor(props) {
        super(props);
        this.state = {
          error: null,
          isLoaded: false,
          company: {},
          address:{}
        };
      }

    componentDidMount() {
        let cachedCleaningProvider = localStorage.getItem("cleaningProvider");
        let cachedAddress = localStorage.getItem("cleaningProviderAddress")
        if (cachedCleaningProvider != null && cachedAddress != null && cachedAddress !== 'undefined') {
            this.setState({
                isLoaded: true,
                company: JSON.parse(cachedCleaningProvider),
                address: JSON.parse(cachedAddress)
            });
            return
        }
        fetch(`${url}/cleaning-providers/${decoded.email}`, {
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
                company: result,
                address:result.address
              });
            localStorage.setItem("cleaningProvider", JSON.stringify(result));
            localStorage.setItem("cleaningProviderAddress", JSON.stringify(result.address));
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
      localStorage.removeItem("Id")
        const {t} = this.props
        if(localStorage.getItem("Token") == null){
            window.location.href='./'
        }else{
        return(
            <div className="profile">
                <div className="profile_back">
                    <p id="cName">{this.state.company.name}</p>
                    <p></p>
                    <p>{t("Email")}: {this.state.company.email}</p>
                    <p></p>
                    <p>{t("Phone")}: {this.state.company.phoneNumber}</p>
                    <p></p>
                    <p>{t("Address")}: {this.state.address.country}, {t("City")}{this.state.address.city}, {t("Street")}{this.state.address.street}, {t("House")}{this.state.address.houseNumber}</p>
                   
                </div>
                <div className="rooms_back">
                    <p>{t("Services")}</p>
                    </div>
                    <div id="rooms_container">
                    <ServiceCard/>
                    </div>
                
            </div>
        )
        }
    }
}

export default withTranslation()  (Profile);
