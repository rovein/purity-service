import React from 'react'
import Header from './Components/auth/HeaderAuth'
import { withTranslation } from 'react-i18next'
import SearchField from './Components/SearchField'
import CleaningCard from './Components/cleaning-provider/CleaningCard'
class Search extends React.Component{
    
    render() { 
        const {t} = this.props
        return(
            <div className="signIn">
            <Header/>
                <div className="search_back">
                    {/* <p id="cName">{t("Search")}</p>
                    <SearchField/> */}
                    <div id="rooms_container">
                    <CleaningCard/>
                    </div>
            </div>
        </div>
        )
    }
}

export default withTranslation() (Search);
