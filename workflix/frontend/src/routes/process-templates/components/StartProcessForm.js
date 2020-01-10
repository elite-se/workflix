// @flow

import React from 'react'
import type { ProcessTemplateMasterDataType } from '../../../modules/datatypes/Process'
import { Button, ButtonGroup, FormGroup, InputGroup } from '@blueprintjs/core'
import { DatePicker } from '@blueprintjs/datetime'
import handleStringChange from '../../../modules/common/handleStringChange'
import AutoSizeTextArea from '../../../modules/common/components/AutoSizeTextArea'
import ProcessApi from '../../../modules/api/ProcessApi'
import AppToaster from '../../../modules/app/AppToaster'
import type { ProcessGroupType } from '../../../modules/datatypes/ProcessGroup'
import { navigate } from '@reach/router'
import ProcessGroupSelect from './ProcessGroupSelect'
import { toastifyError } from '../../../modules/common/toastifyError'
import { getCurrentUserId } from '../../../modules/common/tokenStorage'

type PropsType = {
  template: ProcessTemplateMasterDataType, processGroups: Map<number, ProcessGroupType>, noPadding?: boolean
}

type StateType = {
  title: string, description: string, deadline: Date, startLoading: boolean, processGroup: ?ProcessGroupType
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
    processGroup: null
  }

  componentDidUpdate (prevProps: PropsType): * {
    const template = this.props.template
    if (prevProps.template !== template) {
      this.setState({
        title: template.title,
        description: template.description
      })
    }
  }

  onTitleChange = handleStringChange(title => this.setState({ title }))
  onDescriptionChange = handleStringChange(description => this.setState({ description }))
  onDeadlineChange = (deadline: Date) => this.setState({ deadline })
  onDeadlineButton = (deadline: Date) => () => this.onDeadlineChange(deadline)
  onProcessGroupChange = (processGroup: ProcessGroupType) => this.setState({ processGroup })

  onStartClick = async () => {
    const { title, description, deadline, processGroup } = this.state
    const { template } = this.props
    if (!title || !processGroup || !deadline) {
      return AppToaster.show({
        icon: 'error',
        intent: 'danger',
        message: 'Please fill in all required values.'
      })
    }
    try {
      this.setState({ startLoading: true })
      const { newId } = await new ProcessApi().startProcess({
        starterId: getCurrentUserId(),
        title,
        description,
        deadline,
        processTemplateId: template.id,
        processGroupId: processGroup.id
      })
      this.setState({ startLoading: false })
      navigate(`/processes/${newId}`)
    } catch (e) {
      toastifyError(e)
      this.setState({ startLoading: false })
    }
  }

  render () {
    const { title, description, deadline, startLoading, processGroup } = this.state
    const { processGroups, noPadding } = this.props
    const shortcuts = getShortcuts()
    return <div style={{
      padding: noPadding ? '0px' : '20px',
      width: '460px',
      display: 'flex',
      boxSizing: 'border-box',
      flexDirection: 'column'
    }}>
      <FormGroup label='Title' labelInfo='(required)'>
        <InputGroup large fill placeholder='Title...' intent={!title ? 'danger' : 'none'} value={title}
                    onChange={this.onTitleChange}/>
      </FormGroup>
      <FormGroup label='Description'>
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
    </div>
  }
}

export default StartProcessForm
