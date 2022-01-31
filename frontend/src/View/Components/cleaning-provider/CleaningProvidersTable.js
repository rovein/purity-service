import React from 'react'
import Button from '../ui/Button'
import { withTranslation } from 'react-i18next'
import Loader from "react-loader-spinner";
import * as Constants from "../util/Constants";

const url = Constants.SERVER_URL;

class Card extends React.Component{
    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            companies: {},
            address:{}
        };
      }
    
      componentDidMount() {
        fetch(`${url}/cleaning-providers`, {
                        method: 'get',
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json',
                            'Authorization': 'Bearer ' + localStorage.getItem("Token")
                        }
                    })
          .then(res => res.json())
          .then(
            (result) => {
              this.setState({
                isLoaded: true,
                companies: result
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
        const { error, isLoaded, companies } = this.state;
        if (error) {
          return <div className='additional'>{t("Failiture")}: {error.message}</div>;
        } else if (!isLoaded) {
          return <div className="centered">
            <Loader
              type="Oval" //Audio Oval ThreeDots
              color="#4B0082"
              height={450}
              width={450}
              timeout={10000}
            />
          </div>;
        } else {
          return (
            <table className="w3-table-all w3-centered w3-large">
              <thead>
              <tr color="w3-light-grey">
                <th>{t("DName")}</th>
                <th>{t("Phone")}</th>
                <th>{t("Email")}</th>
                <th>{t("Address")}</th>
                <th></th>
              </tr>
              </thead>
              <tbody>
              {companies.map(this.renderCard)}
              </tbody>
            </table>
          );
        }
      }

    renderCard = (company) => {
      const {t} = this.props
      const {country, city, street, houseNumber} = company.address
      const columnStyle = {verticalAlign: "middle"};
        return (
          <tr className="w3-hover-sand">
            <td style={columnStyle}>{company.name}</td>
            <td style={columnStyle}>{company.phoneNumber}</td>
            <td style={columnStyle}>{company.email}</td>
            <td style={columnStyle}>{country}, {t("City")} {city}, {t("Street")} {street}, {t("House")} {houseNumber}</td>
            <td style={columnStyle}>
              <Button
                className='w3-btn w3-indigo w3-round-small w3-large'
                text={t('SContract')}
                onClick={() => {
                  localStorage.setItem('cleaningMail', company.email);
                  window.location.href = './sign_contract'
                }}/>
            </td>
          </tr>
        );
      };
}

export default withTranslation()  (Card);
