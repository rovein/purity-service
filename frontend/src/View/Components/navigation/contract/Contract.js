import React from 'react'
import Header from '../../auth/HeaderAuth'
import ContractCard from '../../contract/ContractsTable'
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
