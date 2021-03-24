import React from 'react'
import Header from './Components/auth/HeaderAuth'
import AddRForm from './Components/placement-owner/AddRForm'
class Add extends React.Component{
    
    render() { 
        return(
            <div className="signIn">
            <Header/>
                <div className="container">
                    <AddRForm/>
            </div>
        </div>
        )
    }
}

export default Add;
