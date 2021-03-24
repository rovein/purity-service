import React from 'react'

class Button extends React.Component{
    render() {
        return(
                <button 
                className = 'btn'
                type="submit"
                disabled = {this.props.disabled}
                onClick={ () => this.props.onClick()}>
                    {this.props.text}
                </button>
        )
    }
}

export default Button;