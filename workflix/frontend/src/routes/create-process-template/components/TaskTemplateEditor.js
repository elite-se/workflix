// @flow

import React from 'react'
import { Alert, Button, H3, H4, InputGroup } from '@blueprintjs/core'
import type { TaskTemplateType } from '../../../modules/datatypes/Task'
import { difference } from 'lodash'
import PredecessorSelect from './PredecessorSelect'
import SuccessorSelect from './SuccessorSelect'
import styled from 'styled-components'
import AutoSizeTextArea from '../../../modules/common/AutoSizeTextArea'

type PropsType = {
  task: TaskTemplateType,
  onChange: (task: TaskTemplateType) => void,
  allTasks: TaskTemplateType[],
  onDelete: () => void
}

type StateType = {
  deleteAlertOpen: boolean
}

const TrashButton = styled(Button)`
  margin-top: 20px;
`

const findAncestors = (task: TaskTemplateType, allTasks: TaskTemplateType[]) => {
  return [
    task,
    ...task.predecessors
      .map(predId => allTasks.find(_task => _task.id === predId)).filter(Boolean)
      .flatMap(_task => findAncestors(_task, allTasks))
  ]
}

const findDescendants = (task: TaskTemplateType, allTasks: TaskTemplateType[]) => {
  return [
    task,
    ...allTasks.filter(_task => _task.predecessors.includes(task.id))
      .flatMap(_task => findDescendants(_task, allTasks))
  ]
}

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

  render () {
    const { task, allTasks, onChange } = this.props
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
      <p>
        <H4>Name:</H4>
        <InputGroup type='text' placeholder='Name...'
                    value={this.props.task.name}
                    fill
                    onChange={this.onTitleChange}/>
      </p>
      <p>
        <H4>Description:</H4>
        <AutoSizeTextArea placeholder={'Add description...\n\nWhat should be done?\nWhat needs special attention?'}
                          value={this.props.task.description}
                          style={{ resize: 'vertical' }}
                          className='bp3-fill' minRows={4}
                          onChange={this.onDescriptionChange}/>
      </p>
      <p>
        <H4>Duration:</H4>
        <InputGroup type='number' placeholder='Duration...'
                    value={this.props.task.estimatedDuration}
                    fill
                    onChange={this.onDurationChange} min={0.1} step={0.1}/>
      </p>
      <p>
        <H4>Predecessor tasks:</H4>
        <PredecessorSelect allTasks={allTasks} onChange={onChange} possiblePreds={possiblePreds} task={task}/>
      </p>
      <p>
        <H4>Successor tasks:</H4>
        <SuccessorSelect allTasks={allTasks} succs={succs} onChange={onChange} possibleSuccs={possibleSuccs}
                         task={task}/>
      </p>
      <p style={{ textAlign: 'center' }}>
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
      </p>
    </div>
  }
}

export default TaskTemplateEditor
