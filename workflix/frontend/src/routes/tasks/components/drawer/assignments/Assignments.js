// @flow

import React from 'react'
import type { UserType } from '../../../../../modules/datatypes/User'
import type { TaskAssignmentType, TaskType } from '../../../../../modules/datatypes/Task'
import TaskAssignmentSelect from './TaskAssignmentSelect'
import { getCurrentUserId } from '../../../../../modules/common/tokenStorage'
import AssignMeButton from './AssignMeButton'
import SetDoneButton from './SetDoneButton'

type PropsType = {
  task: TaskType,
  onTaskModified: (TaskType) => void,
  users: Map<string, UserType>
}

class Assignments extends React.Component<PropsType> {
  onAssignmentsChanged = (newAssignments: TaskAssignmentType[]) => {
    this.props.onTaskModified({
      ...this.props.task,
      assignments: newAssignments
    })
  }

  canAssign = (user: UserType) => {
    const task = this.props.task
    return task.status !== 'CLOSED' &&
      !task.assignments.find(ass => ass.assigneeId === user.id)
  }

  canSetDone = (user: UserType) => {
    const task = this.props.task
    const assignment = task.assignments.find(ass => ass.assigneeId === user.id)
    return assignment &&
      task.status === 'RUNNING' &&
      !assignment.closed
  }

  render () {
    const { task, users } = this.props
    const curUser = users.get(getCurrentUserId())
    return <>
      <TaskAssignmentSelect task={task} onAssignmentsChanged={this.onAssignmentsChanged} users={users}/>
      {curUser && this.canAssign(curUser) &&
        <AssignMeButton task={task} onAssignmentsChanged={this.onAssignmentsChanged} user={curUser}/>}
      {curUser && this.canSetDone(curUser) &&
        <SetDoneButton task={task} onAssignmentsChanged={this.onAssignmentsChanged} user={curUser}/>}
    </>
  }
}

export default Assignments
