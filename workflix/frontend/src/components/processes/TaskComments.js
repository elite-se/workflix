// @flow

import React from 'react'
import type { TaskType } from '../../datatypes/TaskType'
import CommentBubble from './CommentBubble'

type PropsType = {
  task: TaskType
}

class TaskComments extends React.Component<PropsType> {
  render () {
    const task = this.props.task
    return <div>
      {task.comments.map(comment => {
        return <CommentBubble key={comment.id} comment={comment} />
      })}
    </div>
  }
}

export default TaskComments
