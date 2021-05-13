import React from 'react'
import Button from '../Button'
import { withTranslation } from 'react-i18next'
import Loader from "react-loader-spinner";

var url = "http://localhost:8080"

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
        fetch(`${url}/placement-owners`, {
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
          return <div className="centered" style={{marginLeft: '44%'}}>
            <Loader
              type="Oval" //Audio Oval ThreeDots
              color="#4B0082"
              height={200}
              width={200}
              timeout={10000}
            />
          </div>;
        } else {
          return (
            <table className="w3-table-all w3-centered">
              <thead>
              <tr>
                <th>{t("DName")}</th>
                <th>{t("Phone")}</th>
                <th>{t("Email")}</th>
                <th>Actions</th>
              </tr>
              </thead>
              <tbody>
              {companies.map(this.renderCard)}
              </tbody>
            </table>
          );
        }
      }

      deleteCustomer(email){
        fetch(`${url}/placement-owners/${email}`, {
            method: 'delete',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + localStorage.getItem("Token")
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

    renderCard = (company) => {
      const {t} = this.props
      const columnStyle = {verticalAlign: "middle"};

        return (
          <tr>
            <td style={columnStyle}>{company.name}</td>
            <td style={columnStyle}>{company.phoneNumber}</td>
            <td style={columnStyle}> {company.email}</td>
            <td>
              <Button
                className='w3-btn w3-khaki w3-round-small w3-medium'
                text = {t('Edit')}
                onClick={(e) => {
                  localStorage.setItem("Email",company.email)
                  localStorage.setItem("Role", "PLACEMENT_OWNER")
                  window.location.href='./edit';
                }}/>
              <Button
                className='w3-btn w3-red w3-round-small w3-medium'
                text = {t('Delete')}
                onClick = { () => this.deleteCustomer(company.email)}
              />
            </td>
          </tr>
        );
      };
}

export default withTranslation()  (Card);
