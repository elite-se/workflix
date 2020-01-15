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
  users: Map<string, UserType>,
  isReadOnly: boolean
}

class Assignments extends React.Component<PropsType> {
  onAssignmentsChanged = (newAssignments: TaskAssignmentType[]) => {
    this.props.onTaskModified({
      ...this.props.task,
      assignments: newAssignments
    })
  }

  canAssign = (user: UserType) => {
    const { task, isReadOnly } = this.props
    return !isReadOnly && task.status !== 'CLOSED' &&
      !task.assignments.find(ass => ass.assigneeId === user.id)
  }

  canSetDone = (user: UserType) => {
    const { task, isReadOnly } = this.props
    const assignment = task.assignments.find(ass => ass.assigneeId === user.id)
    return !isReadOnly && assignment &&
      task.status === 'RUNNING' &&
      !assignment.closed
  }

  render () {
    const { task, users, isReadOnly } = this.props
    const curUser = users.get(getCurrentUserId())
    return <>
      <TaskAssignmentSelect task={task} onAssignmentsChanged={this.onAssignmentsChanged} users={users}
                            isReadOnly={isReadOnly}/>
      {curUser && this.canAssign(curUser) &&
        <AssignMeButton task={task} onAssignmentsChanged={this.onAssignmentsChanged} user={curUser}/>}
      {curUser && this.canSetDone(curUser) &&
        <SetDoneButton task={task} onAssignmentsChanged={this.onAssignmentsChanged} user={curUser}/>}
    </>
  }
}

export default Assignments
