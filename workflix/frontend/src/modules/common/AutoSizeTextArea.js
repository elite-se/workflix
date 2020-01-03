// @flow

import React from 'react'
import TextareaAutosize from 'react-textarea-autosize'

class AutoSizeTextArea extends React.Component<React$ElementConfig<'textarea'>> {
  render () {
    return <TextareaAutosize {...this.props}
                             className={this.props.className ? `bp3-input ${this.props.className}` : 'bp3-input'}/>
  }
}

export default AutoSizeTextArea
