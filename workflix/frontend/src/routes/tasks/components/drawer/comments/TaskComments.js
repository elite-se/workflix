// @flow

import React from 'react'
import type { TaskType } from '../../../../../modules/datatypes/Task'
import CommentBubble from './CommentBubble'
import type { UserType } from '../../../../../modules/datatypes/User'
import WriteCommentBubble from './WriteCommentBubble'

type PropsType = {
  task: TaskType,
  users: Map<string, UserType>,
  onTaskModified: (task: TaskType) => void,
  isReadOnly: boolean
}

class TaskComments extends React.Component<PropsType> {
  render () {
    const { task, users, isReadOnly, onTaskModified } = this.props
    return <div>
      {task.comments.map(comment => {
        return <CommentBubble key={comment.id} comment={comment} users={users}/>
      })}
      {!isReadOnly && <WriteCommentBubble task={task} onTaskModified={onTaskModified}/>}
    </div>
  }
}

export default TaskComments
