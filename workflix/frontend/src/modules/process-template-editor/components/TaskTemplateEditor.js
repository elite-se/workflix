// @flow

import React from 'react'
import { Alert, Button, H3, H4, InputGroup } from '@blueprintjs/core'
import { difference } from 'lodash'
import PredecessorSelect from './PredecessorSelect'
import SuccessorSelect from './SuccessorSelect'
import styled from 'styled-components'
import AutoSizeTextArea from '../../common/components/AutoSizeTextArea'
import type { IncompleteTaskTemplateType } from './ProcessTemplateEditor'
import UserRoleSelect from './UserRoleSelect'
import type { UserRoleType } from '../../datatypes/User'

type PropsType = {
  task: IncompleteTaskTemplateType,
  onChange: (task: IncompleteTaskTemplateType) => void,
  allTasks: IncompleteTaskTemplateType[],
  userRoles: Map<number, UserRoleType>,
  onDelete: () => void
}

type StateType = {
  deleteAlertOpen: boolean
}

const TrashButton = styled(Button)`
  margin-top: 20px;
`

const findAncestors = (task: IncompleteTaskTemplateType, allTasks: IncompleteTaskTemplateType[]) => {
  return [
    task,
    ...task.predecessors
      .map(predId => allTasks.find(_task => _task.id === predId)).filter(Boolean)
      .flatMap(_task => findAncestors(_task, allTasks))
  ]
}

const findDescendants = (task: IncompleteTaskTemplateType, allTasks: IncompleteTaskTemplateType[]) => {
  return [
    task,
    ...allTasks.filter(_task => _task.predecessors.includes(task.id))
      .flatMap(_task => findDescendants(_task, allTasks))
  ]
}

const Item = styled<{}, {}, 'div'>('div')`
  margin: 10px 0;
`

class TaskTemplateEditor extends React.Component<PropsType, StateType> {
  state = { deleteAlertOpen: false }

  onTitleChange = (event: SyntheticInputEvent<HTMLInputElement>) => {
    this.props.onChange({
      ...this.props.task,
      name: event.target.value
    })
  }

  onDurationChange = (event: SyntheticInputEvent<HTMLInputElement>) => {
    this.props.onChange({
      ...this.props.task,
      estimatedDuration: Number(event.target.value)
    })
  }

  onDescriptionChange = (event: SyntheticInputEvent<HTMLInputElement>) => {
    this.props.onChange({
      ...this.props.task,
      description: event.target.value
    })
  }

  onOpenDeleteAlert = () => this.setState({ deleteAlertOpen: true })
  onCloseDeleteAlert = () => this.setState({ deleteAlertOpen: false })
  onResponsibleUserRoleChange = (userRole: UserRoleType) => this.props.onChange({
    ...this.props.task,
    responsibleUserRoleId: userRole.id
  })

  render () {
    const { task, allTasks, onChange, userRoles } = this.props
    const { deleteAlertOpen } = this.state

    const possiblePreds = difference(allTasks, findDescendants(task, allTasks))
    const possibleSuccs = difference(allTasks, findAncestors(task, allTasks))
    const succs = allTasks.filter(_task => _task.predecessors.indexOf(task.id) >= 0)

    return <div style={{
      margin: '10px',
      display: 'flex',
      alignItems: 'stretch',
      flexDirection: 'column',
      flex: 1
    }}>
      <H3>Edit Task Template</H3>
      <Item>
        <H4>Name:</H4>
        <InputGroup type='text' placeholder='Name...'
                    value={this.props.task.name}
                    fill
                    onChange={this.onTitleChange}/>
      </Item>
      <Item>
        <H4>Description:</H4>
        <AutoSizeTextArea placeholder={'Add description...\n\nWhat should be done?\nWhat needs special attention?'}
                          value={this.props.task.description}
                          style={{ resize: 'none' }}
                          className='bp3-fill' minRows={4}
                          onChange={this.onDescriptionChange}/>
      </Item>
      <Item>
        <H4>Responsible User Role:</H4>
        <UserRoleSelect userRoles={Array.from(userRoles.values())}
                        activeItem={task.responsibleUserRoleId ? userRoles.get(task.responsibleUserRoleId) : null}
                        onItemSelect={this.onResponsibleUserRoleChange}/>
      </Item>
      <Item>
        <H4>Duration:</H4>
        <InputGroup type='number' placeholder='Duration...'
                    value={this.props.task.estimatedDuration}
                    fill
                    onChange={this.onDurationChange} min={0.1} step={0.1}/>
      </Item>
      <Item>
        <H4>Predecessor tasks:</H4>
        <PredecessorSelect allTasks={allTasks} onChange={onChange} possiblePreds={possiblePreds} task={task}/>
      </Item>
      <Item>
        <H4>Successor tasks:</H4>
        <SuccessorSelect allTasks={allTasks} succs={succs} onChange={onChange} possibleSuccs={possibleSuccs}
                         task={task}/>
      </Item>
      <Item style={{ textAlign: 'center' }}>
        <TrashButton icon='trash' text='Delete Task Template' intent='danger' onClick={this.onOpenDeleteAlert}/>
        <Alert isOpen={deleteAlertOpen} icon='trash' intent='danger' confirmButtonText='Delete' canEscapeKeyCancel
               canOutsideClickCancel cancelButtonText='Cancel' onConfirm={this.props.onDelete}
               onCancel={this.onCloseDeleteAlert}>
          <H4>Delete Task Template?</H4>
          <p>
            Are you sure you want to delete this task template?
            All dependencies of it will be removed as well.
          </p>
        </Alert>
      </Item>
    </div>
  }
}

export default TaskTemplateEditor
