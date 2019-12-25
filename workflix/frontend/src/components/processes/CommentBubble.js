// @flow

import React from 'react'
import type { TaskCommentType } from '../../datatypes/TaskType'
import styled from 'styled-components'

const TalkBubble = styled<{ floatEnd: boolean }, {}, 'div'>('div')`
    margin: 0 10px 10px;
    display: block;
    position: relative;
    float: inline-${props => props.floatEnd ? 'end' : 'start'};
    width: 85%;
    height: auto;
    padding: 5px;
    border: 1px solid #666;
    border-radius: 5px;
    -webkit-border-radius: 5px;
    -moz-border-radius: 5px;
`

type PropsType = {
  comment: TaskCommentType
}

class CommentBubble extends React.Component<PropsType> {
  isOwnComment: boolean = this.props.comment.creatorId.startsWith('ek') // TODO implement real check

  render () {
    const comment = this.props.comment
    return <TalkBubble floatEnd={this.isOwnComment}>
      <small style={{ wordWrap: 'break-word' }}>{comment.creatorId}</small>
      <small style={{ float: 'right', wordWrap: 'break-word' }}>{comment.createdAt}</small>
      <p style={{ wordWrap: 'break-word', hyphens: 'auto' }}><b>{comment.title}</b> {comment.content}</p>
    </TalkBubble>
  }
}

export default CommentBubble
