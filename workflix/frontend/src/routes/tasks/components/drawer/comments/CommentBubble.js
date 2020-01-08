// @flow

import React from 'react'
import type { TaskCommentType } from '../../../../../modules/datatypes/Task'
import type { UserType } from '../../../../../modules/datatypes/User'
import { TalkBubble } from './TalkBubble'
import { getCurrentUserId } from '../../../../../modules/common/tokenStorage'

type PropsType = {
  comment: TaskCommentType,
  users: Map<string, UserType>
}

class CommentBubble extends React.Component<PropsType> {
  alreadySeen = false

  isOwnComment: boolean = this.props.comment.creatorId === getCurrentUserId()

  render () {
    const comment = this.props.comment
    const user = this.props.users.get(comment.creatorId)
    const wasAlreadySeen = this.alreadySeen
    this.alreadySeen = true
    return <TalkBubble
      floatEnd={this.isOwnComment}
      style={{ animationName: wasAlreadySeen || 'pop-up' }}>
      <small style={{ wordWrap: 'break-word' }}>
        {user ? user.name : comment.creatorId}
      </small>
      <small style={{
        float: 'right',
        wordWrap: 'break-word'
      }}>{comment.createdAt.toLocaleString()}</small>
      <p style={{
        wordWrap: 'break-word',
        hyphens: 'auto',
        whiteSpace: 'break-spaces'
      }}>{comment.content}</p>
    </TalkBubble>
  }
}

export default CommentBubble
