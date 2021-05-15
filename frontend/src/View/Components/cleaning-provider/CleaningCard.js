import React from 'react'
import Button from '../ui/Button'
import { withTranslation } from 'react-i18next'

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
          return <div className='additional'>{t("Loading")}...</div>;
        } else {
          return (
            <div className="grid">
              {companies.map(this.renderCard)}
              </div>
          );
        }
      }

    renderCard = (company) => {
      const {t} = this.props
      
        return (
          <div className="card text-center">
                      <div className="crd-body text-dark" id ={company.id}>
                          <h2 className="card-title">{company.name}</h2>
                          <p>{t("Phone")}: {company.phoneNumber}</p>
                          <Button
                            text = {t('SContract')}
                            onClick = { () => {localStorage.setItem('cleaningMail', company.email); window.location.href='./sign_contract'}}
                            />
                      </div>
                  </div>
        );
      };
}

export default withTranslation()  (Card);
