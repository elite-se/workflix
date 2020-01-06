// @flow

import React from 'react'
import type { ProcessTemplateType } from '../../../modules/datatypes/Process'
import { Alert, Button, ButtonGroup, FormGroup, InputGroup } from '@blueprintjs/core'
import { DatePicker } from '@blueprintjs/datetime'
import handleStringChange from '../../../modules/common/handleStringChange'
import AutoSizeTextArea from '../../../modules/common/components/AutoSizeTextArea'
import ProcessApi from '../../../modules/api/ProcessApi'
import AppToaster from '../../../modules/app/AppToaster'
import type { ProcessGroupType } from '../../../modules/datatypes/ProcessGroup'
import { navigate } from '@reach/router'
import ProcessGroupSelect from './ProcessGroupSelect'

type PropsType = { template: ProcessTemplateType, processGroups: Map<number, ProcessGroupType> }

type StateType = {
  title: string, description: string, deadline: Date, startLoading: boolean, processGroup: ?ProcessGroupType,
  errorAlert: ?string
}

const jsDateFormatter = {
  formatDate: date => date.toLocaleDateString(),
  parseDate: str => new Date(str),
  placeholder: 'M/D/YYYY'
}

const getShortcuts = () => [
  /* eslint-disable no-magic-numbers */
  {
    date: new Date(new Date().setDate(new Date().getDate() + 1)),
    label: 'Tomorrow'
  },
  {
    date: new Date(new Date().setDate(new Date().getDate() + 3)),
    label: 'In 3 days'
  },
  {
    date: new Date(new Date().setDate(new Date().getDate() + 7)),
    label: 'In 1 week'
  },
  {
    date: new Date(new Date().setDate(new Date().getDate() + 14)),
    label: 'In 2 weeks'
  },
  {
    date: new Date(new Date().setMonth(new Date().getMonth() + 1)),
    label: 'In 1 month'
  },
  {
    date: new Date(new Date().setMonth(new Date().getMonth() + 3)),
    label: 'In 3 months'
  }
]

class StartProcessForm extends React.Component<PropsType, StateType> {
  state = {
    title: this.props.template.title,
    description: this.props.template.description,
    deadline: new Date(),
    startLoading: false,
    processGroup: null,
    errorAlert: null
  }

  onTitleChange = handleStringChange(title => this.setState({ title }))
  onDescriptionChange = handleStringChange(description => this.setState({ description }))
  onDeadlineChange = (deadline: Date) => this.setState({ deadline })
  onDeadlineButton = (deadline: Date) => () => this.onDeadlineChange(deadline)
  onCloseErrorAlert = () => this.setState({ errorAlert: null })
  onProcessGroupChange = (processGroup: ProcessGroupType) => this.setState({ processGroup })

  onStartClick = async () => {
    const { title, description, deadline, processGroup } = this.state
    const { template } = this.props
    if (!title || !processGroup) {
      return AppToaster.show({
        icon: 'error',
        intent: 'danger',
        message: 'Please fill in all required values.'
      })
    }
    try {
      this.setState({ startLoading: true })
      const { newId } = await new ProcessApi().startProcess({
        starterId: 'test', // todo: Add real starter id here
        title,
        description,
        deadline,
        processTemplateId: template.id,
        processGroupId: processGroup.id
      })
      this.setState({ startLoading: false })
      navigate(`/process/${newId}`)
    } catch (error) {
      this.setState({
        startLoading: false,
        errorAlert: error.message
      })
    }
  }

  render () {
    const { title, description, deadline, startLoading, errorAlert, processGroup } = this.state
    const { processGroups } = this.props
    const shortcuts = getShortcuts()
    return <div style={{
      padding: '20px',
      width: '500px',
      display: 'flex',
      flexDirection: 'column'
    }}>
      <FormGroup label='Title' labelInfo='(required)'>
        <InputGroup large fill placeholder='Title...' intent={!title ? 'danger' : 'none'} value={title}
                    onChange={this.onTitleChange}/>
      </FormGroup>
      <FormGroup label='Description' labelInfo='(required)'>
        <AutoSizeTextArea className='bp3-fill' style={{ resize: 'none' }} placeholder='Description...'
                          value={description} multiline minRows={4} maxRows={12}
                          onChange={this.onDescriptionChange}/>
      </FormGroup>
      <FormGroup label='Process Group' labelInfo='(required)'>
        <ProcessGroupSelect activeItem={processGroup} onItemSelect={this.onProcessGroupChange}
                            items={Array.from(processGroups.values())} intent={!processGroup ? 'danger' : 'none'}/>
      </FormGroup>
      <FormGroup label='Deadline' labelInfo='(required)'>
        <div style={{
          display: 'flex',
          flexDirection: 'row',
          justifyContent: 'start'
        }}>
          <ButtonGroup vertical style={{
            width: '150px',
            marginTop: '35px'
          }} fill minimal>
            {shortcuts.map(sc => <Button key={sc.label}
                                         onClick={this.onDeadlineButton(sc.date)}>{sc.label}</Button>)}
          </ButtonGroup>
          <DatePicker value={deadline} {...jsDateFormatter} minDate={new Date()}
                      onChange={this.onDeadlineChange} maxDate={new Date(new Date().setFullYear(2099))}/>
        </div>
      </FormGroup>
      <Button icon='play' intent='success' text='Start Process' onClick={this.onStartClick} loading={startLoading}/>
      <Alert isOpen={errorAlert !== null} intent='danger' icon='error'
             onClose={this.onCloseErrorAlert}>{errorAlert}</Alert>
    </div>
  }
}

export default StartProcessForm
