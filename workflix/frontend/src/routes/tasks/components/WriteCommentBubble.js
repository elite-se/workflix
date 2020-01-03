// @flow

import React from 'react'
import { TalkBubble } from './TalkBubble'
import { Button } from '@blueprintjs/core'
import ProcessApi from '../../../modules/api/ProcessApi'
import type { TaskCommentType, TaskType } from '../../../modules/datatypes/Task'
import AutoSizeTextArea from '../../../modules/common/AutoSizeTextArea'

type PropsType = {|
  task: TaskType,
  onTaskModified: (TaskType) => void
|}
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

  onKeyPress = (event: KeyboardEvent) => {
    if (this.state.text && event.key === 'Enter' && !event.shiftKey) {
      this.doSend()
      if (event.preventDefault()) { event.preventDefault() }
      return false
    }
  }

  doSend = () => {
    const creatorId = '58c120552c94decf6cf3b722' // TODO set to ID of current user
    const content = this.state.text
    new ProcessApi().addComment(this.props.task.id, creatorId, content)
      .then(response => this.applyNewComment({
        id: response.newId,
        creatorId,
        content,
        createdAt: new Date().toISOString()
      }))
      .catch(err => console.error(err))
    this.setState({
      text: ''
    })
  }

  applyNewComment (newComment: TaskCommentType) {
    const task = this.props.task
    this.props.onTaskModified({
      ...task,
      comments: [...task.comments, newComment]
    })
  }

  render () {
    return <TalkBubble floatEnd
                       onKeyDown={this.onKeyPress}>
      <AutoSizeTextArea
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
        rightIcon='direction-right'
        style={{ float: 'right' }}
        onClick={this.doSend}>
          Send
        </Button>}
    </TalkBubble>
  }
}

export default WriteCommentBubble
