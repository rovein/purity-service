import React from 'react'
import Header from './Components/Header'
import SignUpTabBar from './Components/auth/SignUpTabBar';

class SignUp extends React.Component{
    render() {
        var lng = localStorage.getItem("i18nextLng")
              localStorage.clear();
              localStorage.setItem("i18nextLng", lng)   
        return(
            <div className="signIn">
            <Header/>
                <div className="container">
                <SignUpTabBar/>
            </div>
        </div>
        )
    }
}

export default SignUp;
