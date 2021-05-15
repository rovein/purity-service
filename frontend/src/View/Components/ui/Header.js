import React from 'react'
import { withTranslation } from 'react-i18next'

class Header extends React.Component{
    constructor(props) {
        super(props)
        if(localStorage.getItem("i18nextLng") !=="UA"){
            this.state = {
                value: "EN"
            }
           localStorage.setItem("i18nextLng", "EN")
        }else{
            this.state = {
                value: localStorage.getItem("i18nextLng")
            }
        }
     }

    onLanguageHandle = (event) => {
        if(this.state.value === 'EN'){
            let newLang = 'UA';
            this.setState({value: newLang})
            this.props.i18n.changeLanguage(newLang)
        }else if(this.state.value === 'UA'){
            let newLang = 'EN';
            this.setState({value: newLang})
            this.props.i18n.changeLanguage(newLang)
        }
    }
    render() {
        const {t} = this.props
        return(
            <div className="header">
                <nav>
                    <ul className="nav_links">
                    <li><input type="button" id="locale" value = {this.state.value} onClick={() => { this.onLanguageHandle()}}/></li>
                        <li><a href="/login">{t("Login")}</a></li>
                        <li><a href="/signup">{t("Signup")}</a></li>
                    </ul>
                </nav>
            </div>
        )
    }
}

export default withTranslation() (Header);