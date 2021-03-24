import React from 'react'
import Header from './Components/auth/HeaderAuth'
import EditForm from './Components/auth/EditForm'
class Edit extends React.Component{
    
    render() { 
        return(
            <div className="signIn">
            <Header/>
                <div className="container">
                    <EditForm/>
            </div>
        </div>
        )
    }
}

export default Edit;
