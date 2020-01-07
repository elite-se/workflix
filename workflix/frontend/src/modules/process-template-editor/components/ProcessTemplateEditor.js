// @flow

import React from 'react'
import { Button, ButtonGroup, Drawer, H2, H4 } from '@blueprintjs/core'
import type { UserRoleType, UserType } from '../../datatypes/User'
import ProcessDetailsEditor from './ProcessDetailsEditor'
import type { FilledProcessTemplateType } from '../../api/ProcessApi'
import AppToaster from '../../app/AppToaster'
import TaskTemplateListEditor from './TaskTemplateListEditor'
import type { IncompleteProcessTemplateType, IncompleteTaskTemplateType } from '../ProcessTemplateEditorTypes'
import ButtonWithDialog from '../../common/components/ButtonWithDialog'

type PropsType = {
  users: Map<string, UserType>,
  userRoles: Map<number, UserRoleType>,
  title: string,
  initialProcessTemplate: IncompleteProcessTemplateType,
  onSave: FilledProcessTemplateType => Promise<void>,
  onDelete?: () => Promise<void>,
  showDelete?: boolean
}

type StateType = {|
  ...IncompleteProcessTemplateType,
  highlightValidation: boolean,
  saveLoading: boolean,
  deleteLoading: boolean,
  drawerOpened: boolean
|}

class ProcessTemplateEditor extends React.Component<PropsType, StateType> {
  state = {
    ...this.props.initialProcessTemplate,
    highlightValidation: false,
    saveLoading: false,
    deleteLoading: false,
    drawerOpened: false
  }

  onTitleChange = (title: string) => this.setState({ title })
  onDescriptionChange = (description: string) => this.setState({ description })
  onDurationLimitChange = (durationLimit: number) => this.setState({
    durationLimit: durationLimit > 0 ? durationLimit : null
  })

  onOwnerChange = (owner: ?UserType) => this.setState({ owner })
  onTasksChange = (tasks: IncompleteTaskTemplateType[]) => this.setState({ tasks })

  onSaveClick = async () => {
    const { title, description, durationLimit, owner, tasks } = this.state
    if (!title || !durationLimit || !owner || tasks.length === 0) {
      AppToaster.show({
        icon: 'error',
        message: 'Please fill in all required values.',
        intent: 'danger'
      })
      return this.setState({ highlightValidation: true })
    }
    try {
      this.setState({ saveLoading: true })
      await this.props.onSave({
        title,
        description,
        durationLimit,
        ownerId: owner.id,
        taskTemplates: tasks.map(task => {
          if (!task.responsibleUserRoleId || !task.estimatedDuration) {
            throw new Error(`Task ${task.name} is invalid.`)
          }
          return {
            id: task.id,
            responsibleUserRoleId: task.responsibleUserRoleId,
            name: task.name,
            description: task.description,
            estimatedDuration: task.estimatedDuration || 0,
            necessaryClosings: task.necessaryClosings,
            predecessors: task.predecessors
          }
        })
      })
      this.setState({ saveLoading: false })
    } catch (e) {
      this.setState({ saveLoading: false })
    }
  }

  onDeleteClick = async () => {
    try {
      this.setState({ deleteLoading: true })
      await ((this.props.onDelete && this.props.onDelete()) || Promise.resolve())
      this.setState({ deleteLoading: false })
    } catch (e) {
      this.setState({ deleteLoading: false })
    }
  }

  setDrawerOpened = (drawerOpened: boolean) => this.setState({ drawerOpened })

  render () {
    const {
      tasks, title, description, durationLimit, owner, saveLoading, highlightValidation, drawerOpened, deleteLoading
    } = this.state
    const { users, userRoles, showDelete } = this.props
    return <div style={{
      flex: 1,
      display: 'flex',
      flexDirection: 'column',
      marginRight: drawerOpened ? Drawer.SIZE_SMALL : '0',
      transition: 'margin-right 0.3s'
    }}>
      <ButtonGroup large style={{ marginBottom: '20px' }}>
        <Button icon='floppy-disk' text='Save' intent='primary' loading={saveLoading} onClick={this.onSaveClick}/>
        {showDelete && <ButtonWithDialog intent='danger' icon='trash' text='Delete' loading={deleteLoading}
                                         onClick={this.onDeleteClick}>
          <H4>Delete Process Template?</H4>
          <p>
            Are you sure you want to delete this process template?
          </p>
        </ButtonWithDialog>}
        <H2 style={{
          display: 'inline',
          marginLeft: '40px'
        }}>{this.props.title}</H2>
      </ButtonGroup>
      <ProcessDetailsEditor durationLimit={durationLimit} onDurationLimitChange={this.onDurationLimitChange}
                            onDescriptionChange={this.onDescriptionChange} description={description}
                            onTitleChange={this.onTitleChange} title={title} highlightValidation={highlightValidation}
                            users={users} owner={owner} onOwnerChange={this.onOwnerChange}/>
      <TaskTemplateListEditor tasks={tasks} userRoles={userRoles} onTasksChange={this.onTasksChange}
                              highlightValidation={highlightValidation} setDrawerOpened={this.setDrawerOpened}/>
    </div>
  }
}

export default ProcessTemplateEditor
