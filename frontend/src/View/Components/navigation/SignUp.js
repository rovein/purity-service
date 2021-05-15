import React from 'react'
import Header from '../ui/Header'
import SignUpTabBar from '../auth/SignUpTabBar';

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
