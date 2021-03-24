import React from 'react'
import SearchField from "react-search-field";


class Search extends React.Component{
    render() {
        return(
            <SearchField
            placeholder="Search..."
            // onChange={onChange}
            searchText="This is initial search text"
            classNames="test-class"
          />
        )
    }
}

export default Search; 
