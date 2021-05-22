import React from 'react'
import Header from '../ui/Header'
import LoginForm from '../auth/LoginForm'
class Login extends React.Component{
    
    render() { 
        return(
            <div className="signIn">
            <Header/>
                <div className="container" style={{marginTop: "10%"}}>
                    <LoginForm/>
            </div>
        </div>
        )
    }
}

export default Login;
