import React from 'react'
import Button from '../../ui/Button'
import {withTranslation} from 'react-i18next'
import jwt_decode from "jwt-decode"
import 'react-dropdown/style.css';
import Header from '../../auth/HeaderAuth'
import * as Constants from "../../util/Constants";
import Select from "react-select";
import DatePicker from "react-datepicker"
import "react-datepicker/dist/react-datepicker.css";

const url = Constants.SERVER_URL;
if (localStorage.getItem("Token") != null) {
    var token = localStorage.getItem("Token")
    var decoded = jwt_decode(token)
}

class SignContract extends React.Component {
    constructor(props) {
        super(props)
        let initialDate = new Date();
        this.state = {
            rooms: [],
            roomsSelectOptions : [],
            roomId: 0,
            services: [],
            servicesSelectOptions : [],
            serviceId: 0,
            date: initialDate,
            buttonDisabled: false
        }
    }

    handleRoomChange(e) {
        this.setState({roomId: e.value})
    }


    handleServiceChange(e) {
        this.setState({serviceId: e.value})
    }

    componentDidMount() {
        fetch(`${url}/placement-owners/${decoded.email}/placements`,
            {
                method: 'get',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem('Token')
                }
            }
        )
            .then(res => res.json())
            .then(result => {
                const options = result.map(room => ({
                  "value": room.id,
                  "label": room.id + " - " + room.placementType
                }))
                console.log("Rooms options: ", options)
                this.setState({
                  isLoaded: true,
                  rooms: result,
                  roomsSelectOptions: options
                });
                },
                (error) => {
                    this.setState({
                        isLoaded: true,
                        error
                    });
                }
            );

        fetch(`${url}/cleaning-providers/${localStorage.getItem("cleaningMail")}/services`, {
                method: 'get',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem('Token')
                }
            }
        )
            .then(res => res.json())
            .then(
                (result) => {
                    const options = result.map(service => ({
                        "value": service.id,
                        "label": service.id + " - " + service.name
                    }))
                    console.log("Service options: ", options)
                    this.setState({
                        isLoaded: true,
                        services: result,
                        servicesSelectOptions: options
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

    async signContract() {
        try {
            let res = await fetch(`${url}/contracts`, {
                    method: 'post',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + localStorage.getItem('Token')
                    },
                    body: JSON.stringify({
                        providerServiceId: this.state.serviceId,
                        date: this.state.date,
                        placementId: this.state.roomId
                    })
                }
            )
            let result = await res.json()
            if (result) {
                window.location.href = './contracts';
            }
        } catch (e) {
            console.log(e)
            this.resetForm()
        }
    }

    filterPassedTime = time => {
        const currentDate = new Date();
        const selectedDate = new Date(time);
        const lowerDateRange  =  new Date(time)
        const upperDateRange =  new Date(time)

        lowerDateRange.setHours(9, 0)
        upperDateRange.setHours(19, 0)

        return currentDate.getTime() < selectedDate.getTime() &&
            (selectedDate.getTime() >= lowerDateRange.getTime() && selectedDate.getTime() <= upperDateRange);
    }

    render() {
        const {t} = this.props
        return (
            <div>
                <Header/>
                <div className="container">
                    <div
                        className="w3-container w3-card-4 w3-light-grey w3-text-indigo w3-margin"
                        style={{width: "700px", fontSize: "22px"}}>
                            <h1 className="w3-center">{t('SContract')}</h1>
                            <div>
                                <label>{t("pickSId")}</label>
                                <Select
                                    options={this.state.servicesSelectOptions}
                                    onChange={this.handleServiceChange.bind(this)}
                                    placeholder={t("selectService")}
                                />
                                <label>{t("pickRId")}</label>
                                <Select
                                    options={this.state.roomsSelectOptions}
                                    onChange={this.handleRoomChange.bind(this)}
                                    placeholder={t("selectRoom")}
                                />
                                <label>{t("selectDate")}</label>
                                <br/>
                                <DatePicker
                                    selected={this.state.date}
                                    showTimeSelect
                                    filterTime={this.filterPassedTime}
                                    dateFormat="dd.MM.yyyy HH:mm"
                                    onChange={date => this.setState({date: date})}
                                />
                            </div>
                            <Button
                                className="w3-btn w3-block w3-section w3-indigo w3-padding"
                                text={t('SContract')}
                                disabled={this.state.buttonDisabled}
                                onClick={() => this.signContract()}
                            />
                        </div>
                </div>
            </div>
        )
    }
}

export default withTranslation()(SignContract);
