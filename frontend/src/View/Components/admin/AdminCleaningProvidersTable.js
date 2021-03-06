import React from 'react'
import Button from '../ui/Button'
import { withTranslation } from 'react-i18next'
import Loader from "react-loader-spinner";
import SweetAlert from "react-bootstrap-sweetalert";
import * as Constants from "../util/Constants";

const url = Constants.SERVER_URL;

class Card extends React.Component{
    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            companies: [],
            address:{},
            deleteButtonClicked: false,
            providerEmail: ''
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
            return (<div>
                    <table className="w3-table-all w3-centered w3-hoverable w3-large">
                        <thead>
                        <tr className="w3-light-grey">
                            <th>{t("DName")}</th>
                            <th>{t("Phone")}</th>
                            <th>{t("Email")}</th>
                            <th>{t("IsLocked")}</th>
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
                                  onClick={() => this.deleteCleaning(this.state.providerEmail)}
                              >{t("Delete")}</button>
                          </React.Fragment>
                      }
                  >
                  </SweetAlert>}
              </div>
          );
        }
      }

      deleteCleaning(email){
        this.setState({isLoaded: false})
        fetch(`${url}/cleaning-providers/${email}`, {
            method: 'delete',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' +localStorage.getItem("Token")
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
            <td style={columnStyle}>{company.isLocked
                ? <i className="w3-xxlarge w3-text-red fas fa-times"/>
                : <i className="w3-xxlarge w3-text-green fas fa-check"/>
            }
            </td>
            <td>
              <Button
                  className="w3-btn w3-indigo w3-round-small w3-medium"
                  text={company.isLocked ? t("UnlockUser") : t("LockUser")}
                  onClick={() => this.lockUser(company.email)}
              /> &nbsp;
              <Button
                className='w3-btn w3-khaki w3-round-small w3-medium'
                text = {t('Edit')}
                onClick={(e) => {
                  localStorage.setItem("Email",company.email)
                  localStorage.setItem("Role", "CLEANING_PROVIDER")
                  window.location.href='./edit';
                }}/> &nbsp;
              <Button
                className='w3-btn w3-red w3-round-small w3-medium'
                text = {t('Delete')}
                onClick = { () => this.setState({deleteButtonClicked: true, providerEmail: company.email})}
              />
            </td>
          </tr>
        );
      };

    lockUser(email) {
        this.setState({isLoaded: false})
        fetch(`${url}/admin/lock-user/${email}`, {
            method: "post",
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json",
                Authorization: "Bearer " + localStorage.getItem("Token"),
            },
        }).then(
            (result) => {
                let idx = this.state.companies.findIndex(owner => owner.email === email);
                let owners = this.state.companies;
                owners[idx].isLocked = !owners[idx].isLocked;
                this.setState({companies: owners, isLoaded: true})
            },
            (error) => {
                this.setState({
                    isLoaded: true,
                    error,
                });
            }
        );
    }
}

export default withTranslation()  (Card);
