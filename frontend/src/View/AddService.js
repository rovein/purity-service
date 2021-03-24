import React from 'react'
import Header from './Components/auth/HeaderAuth'
import AddSForm from './Components/cleaning-provider/AddSForm'
class Add extends React.Component{
    
    render() { 
        return(
            <div className="signIn">
            <Header/>
                <div className="container">
                    <AddSForm/>
            </div>
        </div>
        )
    }
}

export default Add;
