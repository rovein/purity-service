import React from 'react'
import Header from '../../auth/HeaderAuth'
import AddRForm from '../../placement-owner/placement/AddPlacementForm'
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
