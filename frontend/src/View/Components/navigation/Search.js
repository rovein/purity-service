import React from 'react'
import Header from '../auth/HeaderAuth'
import { withTranslation } from 'react-i18next'
import SearchField from '../ui/SearchField'
import CleaningCard from '../cleaning-provider/CleaningProvidersTable'
class Search extends React.Component{
    
    render() { 
        const {t} = this.props
        return(
            <div className="signIn">
            <Header/>
                <CleaningCard/>
        </div>
        )
    }
}

export default withTranslation() (Search);
