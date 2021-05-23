import React from 'react'
import Button from '../ui/Button'
import { withTranslation } from 'react-i18next'
import jwt_decode from "jwt-decode"
import Moment from 'moment';
import localization from 'moment/locale/uk'
import Loader from "react-loader-spinner";
import SweetAlert from "react-bootstrap-sweetalert";

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
          services: [],
          date: '',
          deleteButtonClicked: false,
          serviceId: 0
        };
      }
    
      componentDidMount() {
        fetch(`${url}/cleaning-providers/${decoded.email}/services`, {
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
                services: result
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
        const { error, isLoaded, services } = this.state;
        if (error) {
          return <div className='additional'>{t("Failiture")}: {error.message}</div>;
        } else if (!isLoaded) {
          return <div className="centered">
              <Loader
                  type="Oval" //Audio Oval ThreeDots
                  color="#4B0082"
                  height={325}
                  width={325}
                  timeout={10000}
              />
          </div>;
        } else {
            return (
                <div>
                    <table
                        className="w3-table-all w3-centered w3-hoverable w3-large">
                        <thead>
                        <tr className="w3-light-grey">
                            <th>ID</th>
                            <th>{t("DName")}</th>
                            <th>{t("Desc")}</th>
                            <th>{t("minA")}</th>
                            <th>{t("maxA")}</th>
                            <th>{t("rType")}</th>
                            <th>{t("Price")}</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        {services.sort((oneService, anotherService) => {
                            return oneService.id - anotherService.id
                        }).map(this.renderCard)}
                        </tbody>
                    </table>
                    {this.state.deleteButtonClicked && <SweetAlert
                        danger
                        dependencies={[this.state.deleteButtonClicked]}
                        title={t("AreYouSure")}
                        customButtons={
                            <React.Fragment>
                                <button
                                    className="w3-btn w3-light-grey w3-round-small w3-medium"
                                    onClick={() => this.setState({deleteButtonClicked: false})}
                                >{t("Cancel")}</button>
                                &nbsp;
                                <button
                                    className="w3-btn w3-red w3-round-small w3-medium"
                                    onClick={() => this.deleteService(this.state.serviceId)}
                                >{t("Delete")}</button>
                            </React.Fragment>
                        }
                    >{t("NotRecover")}
                    </SweetAlert>
                    }
                </div>
          );
        }
      }

    renderCard = (service) => {
        const {t} = this.props
        const columnStyle = {verticalAlign: "middle"};
        return (
        <tr className="w3-hover-sand">
            <td style={columnStyle}>{service.id}</td>
            <td style={columnStyle}>{service.name}</td>
            <td style={columnStyle}>{service.description}</td>
            <td style={columnStyle}>{service.minArea}</td>
            <td style={columnStyle}>{service.maxArea}</td>
            <td style={columnStyle}>{service.placementType}</td>
            <td style={columnStyle}>{service.pricePerMeter} {t("pMes")}</td>
            <td>
                <a
                    className='w3-btn w3-khaki w3-round-small w3-medium'
                    onClick={(e) => {
                        localStorage.setItem("serviceId", service.id)
                        window.location.href = './edit_service';
                    }}
                >{t('Edit')}</a>&nbsp;
                <a
                    className='w3-btn w3-red w3-round-small w3-medium'
                    onClick={() => this.setState({serviceId: service.id, deleteButtonClicked: true})}
                >{t('Delete')}</a>
            </td>
        </tr>
        );
      };

    deleteService(id){
        this.setState({isLoaded: false})
        fetch(`${url}/cleaning-providers/services/${id}`, {
            method: 'delete',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        })
            .then((result) => {
                    this.setState({
                        services: this.state.services.filter(service => {
                                return service.id !== id
                            }
                        ),
                        isLoaded: true,
                        deleteButtonClicked: false
                    })
                },
                (error) => {
                    this.setState({
                        isLoaded: true,
                        error
                    });
                }
            )
    }
}

export default withTranslation()  (Card);
