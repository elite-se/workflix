// @flow

import React from 'react'
import { Alert, Button, Colors, H3, H4, InputGroup } from '@blueprintjs/core'
import type { TaskTemplateType } from '../../../modules/datatypes/Task'
import { difference } from 'lodash'
import PredecessorSelect from './PredecessorSelect'
import SuccessorSelect from './SuccessorSelect'

type PropsType = {
  task: TaskTemplateType,
  onChange: (task: TaskTemplateType) => void,
  allTasks: TaskTemplateType[],
  onDelete: () => void
}

type StateType = {
  deleteAlertOpen: boolean
}

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
    ...allTasks.filter(_task => _task.predecessors.find(id => id === task.id))
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

  onOpenDeleteAlert = () => this.setState({ deleteAlertOpen: true })
  onCloseDeleteAlert = () => this.setState({ deleteAlertOpen: false })

  render () {
    const { task, allTasks, onChange } = this.props
    const { deleteAlertOpen } = this.state

    const possiblePreds = difference(allTasks, findDescendants(task, allTasks))
    const possibleSuccs = difference(allTasks, findAncestors(task, allTasks))
    const succs = allTasks.filter(_task => _task.predecessors.indexOf(task.id) >= 0)

    return <div style={{
      paddingTop: '10px',
      marginTop: '10px',
      borderTop: `1px ${Colors.GRAY1} solid`,
      display: 'flex',
      alignItems: 'center',
      flexDirection: 'column'
    }}>
      <H3>Edit Task Template</H3>
      <div>
        <H4>Name:</H4>
        <InputGroup type='text' placeholder='Name...'
                    value={this.props.task.name}
                    onChange={this.onTitleChange}/>
      </div>
      <div>
        <H4>Duration:</H4>
        <InputGroup type='number' placeholder='Duration...'
                    value={this.props.task.estimatedDuration}
                    onChange={this.onDurationChange} min={0.1} step={0.1}/>
      </div>
      <div>
        <H4>Predecessor tasks:</H4>
        <PredecessorSelect allTasks={allTasks} onChange={onChange} possiblePreds={possiblePreds} task={task}/>
      </div>
      <div>
        <H4>Successor tasks:</H4>
        <SuccessorSelect allTasks={allTasks} succs={succs} onChange={onChange} possibleSuccs={possibleSuccs}
                         task={task}/>
      </div>
      <div>
        <Button icon='trash' text='Delete Task Template' intent='danger' onClick={this.onOpenDeleteAlert}/>
        <Alert isOpen={deleteAlertOpen} icon='trash' intent='danger' confirmButtonText='Delete' canEscapeKeyCancel
               canOutsideClickCancel cancelButtonText='Cancel' onConfirm={this.props.onDelete}
               onCancel={this.onCloseDeleteAlert}>
          <H4>Delete Task Template?</H4>
          <p>
            Are you sure you want to delete this task template?
            All dependencies of it will be removed as well.
          </p>
        </Alert>
      </div>
    </div>
  }
}

export default TaskTemplateEditor
