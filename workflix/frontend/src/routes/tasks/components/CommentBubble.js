// @flow

import React from 'react'
import type { TaskCommentType } from '../../../modules/datatypes/Task'
import type { UserType } from '../../../modules/datatypes/User'
import { TalkBubble } from './TalkBubble'

type PropsType = {
  comment: TaskCommentType,
  users: Map<string, UserType>
}

class CommentBubble extends React.Component<PropsType> {
  isOwnComment: boolean = this.props.comment.creatorId === ('58c120552c94decf6cf3b722') // TODO implement real check

  render () {
    const comment = this.props.comment
    const user = this.props.users.get(comment.creatorId)
    return <TalkBubble floatEnd={this.isOwnComment}>
      <small style={{ wordWrap: 'break-word' }}>
        {user ? user.name : comment.creatorId}
      </small>
      <small style={{
        float: 'right',
        wordWrap: 'break-word'
      }}>{comment.createdAt}</small>
      <p style={{
        wordWrap: 'break-word',
        hyphens: 'auto'
      }}>{comment.content}</p>
    </TalkBubble>
  }
}

export default CommentBubble
