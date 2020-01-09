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

type PropsType = {|
  id: number,
  users: Map<string, UserType>,
  userRoles: Map<number, UserRoleType>,
  process: ProcessType,
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

  setDrawerOpened = (drawerOpened: boolean) => this.setState({ drawerOpened })

  onSaveClick = () => {
  }

  onAbortClick = () => {
  }

  onTaskModified = () => {
    this.props.update(true)
  }

  render () {
    const { process, template, userRoles, users } = this.props
    const { saveLoading, abortLoading, drawerOpened } = this.state

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
  new ProcessApi().getProcess(id).then(process => Promise.all([
    Promise.resolve(process),
    new ProcessApi().getProcessTemplate(process.processTemplateId)
  ]))
]).then(([users, userRoles, [process, template]]) => ({
  users,
  userRoles,
  process,
  template,
  update
}))

export default withPromiseResolver<PropsType, *>(promiseCreator)(ProcessDetails)
