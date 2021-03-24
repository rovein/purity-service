import React from 'react'
import Button from '../Button'
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
          services: [],
          date: ''
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

      deleteService(id){
        fetch(`${url}/cleaning-providers/services/${id}`, {
            method: 'delete',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        })
        .then((result) => {
            window.location.reload()
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
          return <div className='additional'>{t("Loading")}...</div>;
        } else {
          return (
            <div className="grid">
              {services.map(this.renderCard)}
              </div>
          );
        }
      }

    renderCard = (service) => {
      const {t} = this.props
        return (
          <div className="card text-center">
                      <div className="crd-body text-dark" id ={service.id}>
                          <h2 className="card-title">{service.name}</h2>
                          <p className="card-text text-secondary">{t("Desc")}: {service.description}</p>
                          <p className="card-text text-secondary">{t("minA")}: {service.minArea}</p>
                          <p className="card-text text-secondary">{t("maxA")}: {service.maxArea}</p>
                          <p className="card-text text-secondary">{t("rType")}: {service.placementType}</p>
                          <p className="card-text text-secondary">{t("Price")}: {service.pricePerMeter} {t("pMes")}</p>
                            <Button
                            text = {t('Edit')}
                            onClick={(e) => {
                              localStorage.setItem("serviceId", service.id)
                              window.location.href='./edit_service';
                              }}
                            />
                            <Button
                            text = {t('Delete')}
                            onClick = { () => this.deleteService(service.id)}
                            />
                      </div>
                  </div>
        );
      };
}

export default withTranslation()  (Card);
