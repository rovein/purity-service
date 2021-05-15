import React from 'react'
import { withTranslation } from 'react-i18next';
import jwt_decode from "jwt-decode"

if(localStorage.getItem("Token") != null){
    var token = localStorage.getItem("Token")
    var decoded = jwt_decode(token)
}

class Header extends React.Component{
    signout() {
        var lng = localStorage.getItem("i18nextLng")
          localStorage.clear();
          localStorage.setItem("i18nextLng", lng)   
         window.location.href = '/';
    }

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
        if (localStorage.getItem("Token") == null) {
            window.location.href='./login';
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
        if(decoded.role === "PLACEMENT_OWNER"){
            return(
                <div className="header">
                    <nav>
                        <ul className="nav_links">
                        <li><input type="button" id="locale" value = {this.state.value} onClick={() => { this.onLanguageHandle()}}/></li>
                            <li><a href="/profile" id="PR">{t("Profile")}</a></li>
                            <li><a href="/contracts" id="PR">{t("Contracts")}</a></li>
                            <li><a href="/search" id="PR">{t("Search")}</a></li>
                            <li><a onClick={() => this.signout()} id="SO">{t("Signout")}</a></li>
                        </ul>
                    </nav>
                </div>
            )
        }else if(decoded.role === "CLEANING_PROVIDER"){
            return(
                <div className="header">
                    <nav>
                        <ul className="nav_links">
                        <li><input type="button" id="locale" value = {this.state.value} onClick={() => { this.onLanguageHandle()}}/></li>
                            <li><a href="/profile" id="PR">{t("Profile")}</a></li>
                            <li><a href="/contracts" id="PR">{t("Contracts")}</a></li>
                            <li><a onClick={() => this.signout()} id="SO">{t("Signout")}</a></li>
                        </ul>
                    </nav>
                </div>
            )
        }else if(decoded.role === "ADMIN")
            return(
                <div className="header">
                    <nav>
                        <ul className="nav_links">
                        <li><input type="button" id="locale" value = {this.state.value} onClick={() => { this.onLanguageHandle()}}/></li>
                            <li><a href="/profile" id="PR">{t("Profile")}</a></li>
                            <li><a href="/configure-smart-device" id="SM">{t("ConfigureDevice")}</a></li>
                            <li><a onClick={() => this.signout()} id="SO">{t("Signout")}</a></li>
                        </ul>
                    </nav>
                </div>
            )
        }
    }


export default withTranslation() (Header);
