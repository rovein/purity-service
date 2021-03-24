import React from 'react'
import Header from './Components/auth/HeaderAuth'
import ContractCard from './Components/admin/ContractCard'
class Contract extends React.Component{
    
    render() { 
        return(
            <div className="signIn">
            <Header/>
            <div id="rooms_container">
                    <ContractCard/>
                    </div>
        </div>
        )
    }
}

export default Contract;
