// @flow

import React from 'react'
import { TalkBubble } from './TalkBubble'
import { Button, TextArea } from '@blueprintjs/core'

type PropsType = {||}
type StateType = {|
  text: string
|}

class WriteCommentBubble extends React.Component<PropsType, StateType> {
  state = {
    text: ''
  }

  onTextChange = (event: SyntheticInputEvent<HTMLInputElement>) => {
    this.setState({
      text: event.target.value
    })
  }

  onSendClick = () => {
    this.setState({
      text: ''
    })
  }

  render () {
    return <TalkBubble floatEnd>
      <TextArea
        fill
        growVertically
        style={{
          resize: 'none',
          border: 'none',
          outline: 'none',
          boxShadow: 'none'
        }}
        placeholder='Type to add a comment …'
        rows={1}
        onChange={this.onTextChange}
        value={this.state.text}
      />
      {this.state.text &&
        <Button
        icon='envelope'
        style={{ float: 'right' }}
        onClick={this.onSendClick}>
          Send
        </Button>}
    </TalkBubble>
  }
}

export default WriteCommentBubble
