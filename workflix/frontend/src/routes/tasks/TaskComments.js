// @flow

import React from 'react'
import type { TaskType } from '../../modules/datatypes/Task'
import CommentBubble from './CommentBubble'
import type { UserType } from '../../modules/datatypes/User'

type PropsType = {
  task: TaskType,
  users: Map<string, UserType>
}

class TaskComments extends React.Component<PropsType> {
  render () {
    const task = this.props.task
    return <div>
      {task.comments.map(comment => {
        return <CommentBubble key={comment.id} comment={comment} users={this.props.users}/>
      })}
    </div>
  }
}

export default TaskComments
