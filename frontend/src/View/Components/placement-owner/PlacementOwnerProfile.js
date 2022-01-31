import React from 'react'
import {withTranslation} from 'react-i18next';

import jwt_decode from "jwt-decode"
import PlacementsTable from './placement/PlacementsTable';
import * as Constants from "../util/Constants";

const url = Constants.SERVER_URL;
if (localStorage.getItem("Token") != null) {
    var token = localStorage.getItem("Token")
    var decoded = jwt_decode(token)
}

class Profile extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            company: {},
            address: {}
        };
    }

    componentDidMount() {
        let cachedPlacementOwner = localStorage.getItem("placementOwner");
        let cachedAddress = localStorage.getItem("placementOwnerAddress")
        if (cachedPlacementOwner != null && cachedAddress != null && cachedAddress !== 'undefined') {
            this.setState({
                isLoaded: true,
                company: JSON.parse(cachedPlacementOwner),
                address: JSON.parse(cachedAddress)
            });
            return
        }
        fetch(`${url}/placement-owners/${decoded.email}`, {
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
                        address: result.address
                    });
                    localStorage.setItem("placementOwner", JSON.stringify(result));
                    localStorage.setItem("placementOwnerAddress", JSON.stringify(result.address));
                },
                (error) => {
                    this.setState({
                        isLoaded: true,
                        error: error
                    });
                }
            )
    }

    render() {
        localStorage.removeItem("Id")
        const {t} = this.props
        if (localStorage.getItem("Token") == null) {
            window.location.href = './'
        } else {
            return (
                <div className="profile">
                    <div className="profile_back">
                        <p id="cName">{this.state.company.name}</p>
                        <p></p>
                        <p>{t("Email")}: {this.state.company.email}</p>
                        <p></p>
                        <p>{t("Phone")}: {this.state.company.phoneNumber}</p>
                        <p></p>
                        <p>
                            {t("Address")}: {this.state.address.country}, {
                            t("City")} {this.state.address.city}, {
                            t("Street")} {this.state.address.street}, {
                            t("House")} {this.state.address.houseNumber}
                        </p>
                    </div>
                    <div className="rooms_back">
                        <p>{t("Rooms")}</p>
                    </div>
                    <div id="rooms_container">
                        <PlacementsTable/>
                    </div>
                </div>
            )
        }
    }
}

export default withTranslation()(Profile);
