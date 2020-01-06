// @flow

import React from 'react'
import { FormGroup, H3, H4, InputGroup, NumericInput } from '@blueprintjs/core'
import { difference } from 'lodash'
import PredecessorSelect from './PredecessorSelect'
import SuccessorSelect from './SuccessorSelect'
import styled from 'styled-components'
import AutoSizeTextArea from '../../common/components/AutoSizeTextArea'
import UserRoleSelect from './UserRoleSelect'
import type { UserRoleType } from '../../datatypes/User'
import type { IncompleteTaskTemplateType } from '../ProcessTemplateEditorTypes'
import handleStringChange from '../../common/handleStringChange'
import ButtonWithDialog from '../../common/components/ButtonWithDialog'

type PropsType = {
  task: IncompleteTaskTemplateType,
  onChange: (task: IncompleteTaskTemplateType) => void,
  allTasks: IncompleteTaskTemplateType[],
  userRoles: Map<number, UserRoleType>,
  highlightValidation: boolean,
  onDelete: () => void
}

const TrashButtonWithDialog = styled(ButtonWithDialog)`
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

class TaskTemplateEditor extends React.Component<PropsType> {
  onTitleChange = handleStringChange(name => this.props.onChange({
    ...this.props.task,
    name
  }))

  onDurationChange = (value: number) => this.props.onChange({
    ...this.props.task,
    estimatedDuration: value > 0 ? value : null
  })

  onDescriptionChange = handleStringChange(description => this.props.onChange({
    ...this.props.task,
    description
  }))

  onResponsibleUserRoleChange = (userRole: UserRoleType) => this.props.onChange({
    ...this.props.task,
    responsibleUserRoleId: userRole.id
  })

  render () {
    const { task, allTasks, onChange, userRoles, highlightValidation, onDelete } = this.props

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
      <FormGroup label='Name' labelInfo='(required)'>
        <InputGroup type='text' placeholder='Name...' value={task.name}
                    fill onChange={this.onTitleChange} intent={highlightValidation && !task.name ? 'danger' : 'none'}/>
      </FormGroup>
      <FormGroup label='Description'>
        <AutoSizeTextArea placeholder={'Add description...\n\nWhat should be done?\nWhat needs special attention?'}
                          value={this.props.task.description} style={{ resize: 'none' }} className='bp3-fill'
                          minRows={4} onChange={this.onDescriptionChange}/>
      </FormGroup>
      <FormGroup label='Responsible User Role' labelInfo='(required)'>
        <UserRoleSelect userRoles={Array.from(userRoles.values())}
                        intent={highlightValidation && !task.responsibleUserRoleId ? 'danger' : 'none'}
                        activeItem={task.responsibleUserRoleId ? userRoles.get(task.responsibleUserRoleId) : null}
                        onItemSelect={this.onResponsibleUserRoleChange}/>
      </FormGroup>
      <FormGroup label='Duration' labelInfo='(required, positive)'>
        <NumericInput placeholder='Duration...' value={task.estimatedDuration || ''}
                      intent={highlightValidation && (!task.estimatedDuration || task.estimatedDuration <= 0) ? 'danger' : 'none'}
                      fill onValueChange={this.onDurationChange} min={0.1} stepSize={0.1}/>
      </FormGroup>
      <FormGroup label='Predecessor Tasks'>
        <PredecessorSelect allTasks={allTasks} onChange={onChange} possiblePreds={possiblePreds} task={task}/>
      </FormGroup>
      <FormGroup label='Successor Tasks'>
        <SuccessorSelect allTasks={allTasks} succs={succs} onChange={onChange} possibleSuccs={possibleSuccs}
                         task={task}/>
      </FormGroup>
      <Item style={{ textAlign: 'center' }}>
        <TrashButtonWithDialog icon='trash' text='Delete Task Template' intent='danger'
                               onClick={onDelete}>
          <H4>Delete Task Template?</H4>
          <p>
            Are you sure you want to delete this task template?
            All dependencies of it will be removed as well.
          </p>
        </TrashButtonWithDialog>
      </Item>
    </div>
  }
}

export default TaskTemplateEditor
