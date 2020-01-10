// @flow

import React from 'react'
import type { StyledComponent } from 'styled-components'
import styled from 'styled-components'
import { FormGroup, Icon } from '@blueprintjs/core'
import type { TaskTemplateType, TaskType } from '../../../../modules/datatypes/Task'
import type { UserRoleType, UserType } from '../../../../modules/datatypes/User'
import TaskComments from './comments/TaskComments'
import Assignments from './assignments/Assignments'

const StyledContainer = styled<{}, {}, 'div'>('div')`
  margin: 8px;
  display: flex;
  flex-direction: column;
`

const Item: StyledComponent<{}, {}, *> = styled('div')`
  display: flex;
  flex-direction: row;
  align-items: center;
  & > * {
    padding: 5px;
  }
`
type PropsType = {
  task: TaskType,
  taskTemplates: Map<number, TaskTemplateType>,
  onTaskModified: (TaskType) => void,
  users: Map<string, UserType>,
  userRoles: Map<number, UserRoleType>
}

class TaskDrawerContent extends React.Component<PropsType> {
  render () {
    const { task, taskTemplates, userRoles, onTaskModified, users } = this.props
    const taskTemplate = taskTemplates.get(task.taskTemplateId)
    if (!taskTemplate) {
      return <div>Something went wrong.</div>
    }
    const userRole = userRoles.get(taskTemplate.responsibleUserRoleId)
    if (!userRole) {
      return <div>Something went wrong.</div>
    }
    return <StyledContainer>
      <FormGroup label='Description'>
        <div style={{ padding: '10px' }}>{taskTemplate.description}</div>
      </FormGroup>
      <FormGroup label='Details'>
        <Item><Icon icon='people'/>Preferred user role: {userRole.name}</Item>
        <Item><Icon icon='stopwatch'/>Estimated duration: {taskTemplate.estimatedDuration}</Item>
        <Item><Icon icon='numerical'/>Necessary Users: {taskTemplate.necessaryClosings}</Item>
      </FormGroup>
      <FormGroup label='Assignees'>
        <Assignments task={task} onTaskModified={onTaskModified} users={users}/>
      </FormGroup>
      <FormGroup label='Comments'>
        <TaskComments task={task} users={users} onTaskModified={onTaskModified}/>
      </FormGroup>
    </StyledContainer>
  }
}

export default TaskDrawerContent
