import React from 'react'
import Button from './Components/Button'
import Dropdown from 'react-dropdown';
import {withTranslation} from 'react-i18next'
import jwt_decode from "jwt-decode"
import 'react-dropdown/style.css';
import Header from './Components/auth/HeaderAuth'

const url = "http://localhost:8080";
if (localStorage.getItem("Token") != null) {
    var token = localStorage.getItem("Token")
    var decoded = jwt_decode(token)
}

class SignContract extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            rooms: '',
            roomId: 0,
            roomsId: [],
            services: [],
            serviceId: 0,
            servicesId: [],
            buttonDisabled: false
        }
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
                    this.setState({
                        isLoaded: true,
                        rooms: result
                    });
                    this.state.rooms.forEach(element => {
                        this.state.roomsId.push(element.id)
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
                    this.setState({
                        isLoaded: true,
                        services: result
                    });
                    console.log(this.state.services)
                    this.state.services.forEach(element => {
                        this.state.servicesId.push(element.id)
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
                        date: new Date(),
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

    handleRChange = (event) => {
        this.setState({
            roomId: event.value
        });
    }

    handleSChange = (event) => {
        this.setState({
            serviceId: event.value
        });
    }

    render() {
        const {t} = this.props
        return (
            <div className="signIn">
                <Header/>
                <div className="container">
                    <div className="signInForm">
                        <div className='signInContainer'>
                            <h1>{t('SContract')}</h1>
                            <div>
                                <p>{t("pickSId")}</p>
                                <Dropdown options={this.state.servicesId}
                                          onChange={this.handleSChange}
                                          value={this.state.value}
                                          placeholder={t("Select an id")}/>
                                <p>{t("pickRId")}</p>
                                <Dropdown options={this.state.roomsId}
                                          onChange={this.handleRChange}
                                          value={this.state.value}
                                          placeholder={t("Select an id")}/>
                            </div>
                            <Button
                                text={t('SContract')}
                                disabled={this.state.buttonDisabled}
                                onClick={() => this.signContract()}
                            />
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default withTranslation()(SignContract);
