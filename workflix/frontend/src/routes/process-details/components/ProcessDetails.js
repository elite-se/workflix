// @flow

import React from 'react'
import UserApi from '../../../modules/api/UsersApi'
import type { UserRoleType, UserType } from '../../../modules/datatypes/User'
import withPromiseResolver from '../../../modules/app/hocs/withPromiseResolver'
import ProcessApi from '../../../modules/api/ProcessApi'
import type { ProcessTemplateType, ProcessType } from '../../../modules/datatypes/Process'
import { Button, ButtonGroup, Drawer, H2, H4 } from '@blueprintjs/core'
import ButtonWithDialog from '../../../modules/common/components/ButtonWithDialog'
import TaskListViewer from './TaskListViewer'
import { toastifyError } from '../../../modules/common/toastifyError'
import { navigate } from '@reach/router'
import ProcessDetailsEditor from './ProcessDetailsEditor'
import AppToaster from '../../../modules/app/AppToaster'
import type { ProcessGroupType } from '../../../modules/datatypes/ProcessGroup'
import ProcessGroupsApi from '../../../modules/api/ProcessGroupsApi'

type PropsType = {|
  id: number,
  users: Map<string, UserType>,
  userRoles: Map<number, UserRoleType>,
  process: ProcessType,
  processGroups: Map<number, ProcessGroupType>,
  template: ProcessTemplateType,
  update: (soft: boolean) => void
|}

type StateType = {
  title: string,
  description: string,
  deadline: Date,
  saveLoading: boolean,
  abortLoading: boolean,
  drawerOpened: boolean
}

class ProcessDetails extends React.Component<PropsType, StateType> {
  state = {
    title: this.props.process.title,
    description: this.props.process.description,
    deadline: this.props.process.deadline,
    saveLoading: false,
    abortLoading: false,
    drawerOpened: false
  }

  onTitleChange = (title: string) => this.setState({ title })
  onDescriptionChange = (description: string) => this.setState({ description })
  onDeadlineChange = (deadline: Date) => this.setState({ deadline })

  setDrawerOpened = (drawerOpened: boolean) => this.setState({ drawerOpened })

  onSaveClick = async () => {
    const { title, deadline, description } = this.state
    if (!title || !deadline) {
      AppToaster.show({
        intent: 'danger',
        icon: 'error',
        message: 'Please fill in all required values.'
      })
      return
    }
    this.setState({ saveLoading: true })
    try {
      await new ProcessApi().patchProcess(this.props.process.id, title, description, deadline)
      this.setState({ saveLoading: false })
      navigate('/processes')
    } catch (e) {
      toastifyError(e)
      this.setState({ saveLoading: false })
    }
  }

  onAbortClick = async () => {
    this.setState({ abortLoading: true })
    try {
      await new ProcessApi().abortProcess(this.props.process.id)
      this.setState({ abortLoading: false })
      navigate('/processes')
    } catch (e) {
      toastifyError(e)
      this.setState({ abortLoading: false })
    }
  }

  onTaskModified = () => {
    this.props.update(true)
  }

  render () {
    const { process, template, userRoles, users, processGroups } = this.props
    const { saveLoading, abortLoading, drawerOpened, title, description, deadline } = this.state

    return <div style={{
      flex: 1,
      display: 'flex',
      flexDirection: 'column',
      marginRight: drawerOpened ? Drawer.SIZE_SMALL : '0',
      transition: 'margin-right 0.3s'
    }}>
      <ButtonGroup large style={{ marginBottom: '20px' }}>
        <Button icon='floppy-disk' text='Save' intent='primary' loading={saveLoading} onClick={this.onSaveClick}/>
        <ButtonWithDialog intent='danger' icon='ban-circle' text='Abort' loading={abortLoading}
                          disabled={process.status !== 'RUNNING'}
                          onClick={this.onAbortClick}>
          <H4>Abort Process?</H4>
          <p>
            Are you sure you want to abort this process?
          </p>
        </ButtonWithDialog>
        <H2 style={{
          display: 'inline',
          marginLeft: '40px'
        }}>Process Details</H2>
      </ButtonGroup>
      <ProcessDetailsEditor onTitleChange={this.onTitleChange} title={title} process={process} deadline={deadline}
                            onDeadlineChange={this.onDeadlineChange} onDescriptionChange={this.onDescriptionChange}
                            description={description} users={users} processGroups={processGroups} template={template}/>
      <TaskListViewer tasks={process.tasks}
                      users={users}
                      taskTemplates={new Map(template.taskTemplates.map(template => [template.id, template]))}
                      userRoles={userRoles}
                      onTaskModified={this.onTaskModified}
                      setDrawerOpened={this.setDrawerOpened}/>
    </div>
  }
}

const promiseCreator = ({ id }: { id: number }, update) => Promise.all([
  new UserApi().getUsers(),
  new UserApi().getUserRoles(),
  new ProcessGroupsApi().getProcessGroups(),
  new ProcessApi().getProcess(id).then(process => Promise.all([
    Promise.resolve(process),
    new ProcessApi().getProcessTemplate(process.processTemplateId)
  ]))
]).then(([users, userRoles, processGroups, [process, template]]) => ({
  users,
  userRoles,
  processGroups,
  process,
  template,
  update
}))

export default withPromiseResolver<PropsType, *>(promiseCreator)(ProcessDetails)
