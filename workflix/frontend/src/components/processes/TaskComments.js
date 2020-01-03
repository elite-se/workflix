// @flow

import React from 'react'
import type { TaskType } from '../../datatypes/TaskType'
import CommentBubble from './CommentBubble'
import type { UserType } from '../../datatypes/models'

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
