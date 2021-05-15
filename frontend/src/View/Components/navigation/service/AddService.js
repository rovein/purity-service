import React from 'react'
import Header from '../../auth/HeaderAuth'
import AddSForm from '../../cleaning-provider/AddSForm'
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
