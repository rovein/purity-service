import React from 'react'
import Button from '../Button'
import { withTranslation } from 'react-i18next';

import jwt_decode from "jwt-decode"
import ServiceCard from './ServiceCard';

if(localStorage.getItem("Token") != null){
    var token = localStorage.getItem("Token")
    var decoded = jwt_decode(token)
}

var url = "http://localhost:8080"

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
                    <Button
                        text = {t("AddS")}
                        disabled = {false}
                        onClick={(e) => {
                            window.location.href='./add_service';
                            }}
                    />
                    <p>{t("Phone")}: {this.state.company.phoneNumber}</p>
                    <Button
                        text = {t('EditP')}
                        disabled = {false}
                        onClick = { () => {window.location.href='./edit';}}
                    />
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
