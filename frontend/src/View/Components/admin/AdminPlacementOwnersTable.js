import React from 'react'
import Button from '../ui/Button'
import { withTranslation } from 'react-i18next'
import Loader from "react-loader-spinner";
import SweetAlert from "react-bootstrap-sweetalert";

var url = "http://localhost:8080"

class Card extends React.Component{
    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            companies: [],
            address:{},
            deleteButtonClicked: false,
            ownerEmail: ''
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
              type="Oval"
              color="#4B0082"
              height={200}
              width={200}
              timeout={10000}
            />
          </div>;
        } else {
          return (
              <div>
                  <table className="w3-table-all w3-centered w3-hoverable w3-large">
                      <thead>
                      <tr className="w3-light-grey">
                          <th>{t("DName")}</th>
                          <th>{t("Phone")}</th>
                          <th>{t("Email")}</th>
                          <th></th>
                      </tr>
                      </thead>
                      <tbody>
                      {companies.map(this.renderCard)}
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
                                  onClick={() => this.deleteCustomer(this.state.ownerEmail)}
                              >{t("Delete")}</button>
                          </React.Fragment>
                      }
                  >
                  </SweetAlert>}
              </div>
          );
        }
      }

      deleteCustomer(email){
        this.setState({isLoaded: false})
        fetch(`${url}/placement-owners/${email}`, {
            method: 'delete',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + localStorage.getItem("Token")
            }
        })
        .then((result) => {
                this.setState({
                    companies: this.state.companies.filter(company => {
                            return company.email !== email
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

    renderCard = (company) => {
      const {t} = this.props
      const columnStyle = {verticalAlign: "middle"};
        return (
          <tr className="w3-hover-sand">
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
                }}/> &nbsp;
              <Button
                className='w3-btn w3-red w3-round-small w3-medium'
                text = {t('Delete')}
                onClick = { () => this.setState({deleteButtonClicked: true, ownerEmail: company.email})}
              />
            </td>
          </tr>
        );
      };
}

export default withTranslation()  (Card);
