import React from 'react'
import Header from './Components/Header'
import LoginForm from './Components/auth/LoginForm'
class Login extends React.Component{
    
    render() { 
        return(
            <div className="signIn">
            <Header/>
                <div className="container">
                    <LoginForm/>
            </div>
        </div>
        )
    }
}

export default Login;
