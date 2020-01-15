// @flow

import React from 'react'
import type { TaskType } from '../../../../../modules/datatypes/Task'
import CommentBubble from './CommentBubble'
import type { UserType } from '../../../../../modules/datatypes/User'
import WriteCommentBubble from './WriteCommentBubble'

type PropsType = {
  task: TaskType,
  users: Map<string, UserType>,
  onTaskModified: (task: TaskType) => void
}

class TaskComments extends React.Component<PropsType> {
  render () {
    const { task, users, onTaskModified } = this.props
    return <div>
      {task.comments.map(comment => {
        return <CommentBubble key={comment.id} comment={comment} users={users}/>
      })}
      <WriteCommentBubble task={task} onTaskModified={onTaskModified}/>
    </div>
  }
}

export default TaskComments
