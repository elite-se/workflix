// @flow

import React from 'react'
import type { TaskTemplateType, TaskType } from '../../modules/datatypes/Task'
import styled from 'styled-components'
import TaskAssignmentSelect from './TaskAssignmentSelect'
import TaskComments from './TaskComments'
import type { UserType } from '../../modules/datatypes/User'

const StyledContainer = styled<{}, {}, 'div'>('div')`
  margin: 8px;
`

type PropsType = {
  task: TaskType,
  taskTemplates: Map<number, TaskTemplateType>,
  onTaskModified: (TaskType) => void,
  users: Map<string, UserType>
}

class TaskDrawerContent extends React.Component<PropsType> {
  render () {
    const task = this.props.task
    const taskTemplate = this.props.taskTemplates.get(task.taskTemplateId)
    return <StyledContainer>
      <h4>Description</h4>
      <p>{taskTemplate ? taskTemplate.description : ''}</p>

      <h4>Assignee</h4>
      <TaskAssignmentSelect task={task} onTaskModified={this.props.onTaskModified} users={this.props.users}/>

      <h4>Comments</h4>
      <TaskComments task={task} users={this.props.users}/>
    </StyledContainer>
  }
}

export default TaskDrawerContent
